package com.mrKhoai.webshop.beta;

import com.mrKhoai.webshop.controller.WebshopConst;
import com.mrKhoai.webshop.controller.role.RoleService;
import com.mrKhoai.webshop.controller.customer.CustomerService;
import com.mrKhoai.webshop.controller.staff.StaffService;
import com.mrKhoai.webshop.objects.Customer;
import com.mrKhoai.webshop.objects.Staff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UserService implements UserDetailsService {

    @Autowired
    private StaffService staffService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CustomerService customerService;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    /**
     * load User by ussername
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        username = username.toLowerCase();
        Staff staff = staffService.findByName(username);
        if(staff == null) {
            Customer customer = customerService.findByName(username);
            if (customer == null) {
                UserBuilder builder = User.withUsername(customer.getCustomerName());
                builder.password(customer.getPassword());
                builder.authorities(WebshopConst.ROLE_CUSTOMER);
                return builder.build();
            } else {
                throw new UsernameNotFoundException(WebshopConst.USER_NOT_FOUND);
            }
        } else {
            UserBuilder builder = User.withUsername(staff.getStaffName());
            builder.password(staff.getPassword());
            String role = staff.getRole().getRoleName().toUpperCase();
            builder.authorities(WebshopConst.ROLE + role);
            LOGGER.info("authorized {} as role {} ",username, role);
            LOGGER.info("username: {} password: {}", builder.build().getUsername(), builder.build().getPassword());
            LOGGER.info(new BCryptPasswordEncoder().matches("Duy***1994", builder.build().getPassword()));
            return builder.build();
        }
    }
}
