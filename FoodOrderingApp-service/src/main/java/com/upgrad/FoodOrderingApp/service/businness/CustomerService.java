package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.Customer;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuth;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional
    public Customer signup(Customer customer) throws SignUpRestrictedException {
        Customer cust = customerDao.findByContactNo(customer.getContactNumber());
        if (cust != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }
        if (customer.getContactNumber() == null || customer.getFirstName() == null || customer.getEmail() == null || customer.getPassword() == null) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");

        }
        String regexEmail = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(customer.getEmail());
        if(!(matcher.matches())){
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }

        String regexMobileNo = "[0-9]{10}";
        Pattern pattern1 = Pattern.compile(regexMobileNo);
        Matcher matcher1 = pattern1.matcher(customer.getContactNumber());
        if(!(matcher1.matches())){
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }

        String passwordRegex = "((?=.*\\d)(?=.*[A-Z])(?=.*[@#$%]).{8,})";
        Pattern pattern2 = Pattern.compile(passwordRegex);
        Matcher matcher2 = pattern2.matcher(customer.getPassword());
        if(!(matcher2.matches())){
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }

        String[] encryptedText = passwordCryptographyProvider.encrypt(customer.getPassword());
        customer.setSalt(encryptedText[0]);
        customer.setPassword(encryptedText[1]);
        return customerDao.createCustomer(customer);
    }

    @Transactional
    public CustomerAuth authenticate(final String username, final String password) throws AuthenticationFailedException {
        Customer customer = customerDao.findByContactNo(username);
        if (customer == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        CustomerAuth customerAuthentication = customerAuthDao.findByCustomer(customer);
        if(customerAuthentication == null){
        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, customer.getSalt());
            if (encryptedPassword.equals(customer.getPassword())) {
                JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
                CustomerAuth customerAuth = new CustomerAuth();
                customerAuth.setCustomer(customer);
                customerAuth.setUuid(customer.getUuid());
                customerAuth.setAccessToken(jwtTokenProvider.generateToken(customer.getUuid(), now, expiresAt));
                customerAuth.setLoginAt(now);
                customerAuth.setExpiresAt(expiresAt);

                customerDao.createAuthToken(customerAuth);

                return customerAuth;
            } else {
                throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
            }
        }
        else{
            customerAuthentication.setLoginAt(now);
            customerAuthentication.setExpiresAt(expiresAt);
            customerAuthentication.setLogoutAt(null);
            return customerAuthentication;
        }
    }

    @Transactional
    public Customer logout(String accessToken) throws AuthorizationFailedException {
        CustomerAuth customerAuth = customerAuthDao.findByAccessToken(accessToken);
        if(customerAuth == null){
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if(customerAuth.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime zdt = ZonedDateTime.now();
        if(customerAuth.getExpiresAt().isBefore(zdt)){
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        customerAuth.setLogoutAt(zdt);
        customerAuthDao.updateLogout(customerAuth);
        Customer customer = customerDao.findById(customerAuth.getCustomer().getId());
        return customer;
    }

    @Transactional
    public Customer updateCustomer(String accessToken, Customer customer) throws AuthorizationFailedException, UpdateCustomerException {
        CustomerAuth customerAuth = customerAuthDao.findByAccessToken(accessToken);
        if(customerAuth == null){
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if(customerAuth.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime zdt = ZonedDateTime.now();
        if(customerAuth.getExpiresAt().isBefore(zdt)){
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        if(customer.getFirstName().isEmpty()){
            throw new UpdateCustomerException("UCR-002","First name field should not be empty");
        }
        Customer customer1 = customerDao.findById(customerAuth.getCustomer().getId());
        customer1.setFirstName(customer.getFirstName());
        customer1.setLastName(customer.getLastName());
        customerDao.updateCustomerDetails(customer1);
        return customer;

    }

    @Transactional
    public Customer changePassword(String accessToken, String oldPassword, String newPassword) throws AuthorizationFailedException, UpdateCustomerException {
        CustomerAuth customerAuth = customerAuthDao.findByAccessToken(accessToken);
        if(customerAuth == null){
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if(customerAuth.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime zdt = ZonedDateTime.now();
        if(customerAuth.getExpiresAt().isBefore(zdt)){
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        if(oldPassword.isEmpty() || newPassword.isEmpty()){
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
        String passwordRegex = "((?=.*\\d)(?=.*[A-Z])(?=.*[@#$%]).{8,})";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(newPassword);
        if(!(matcher.matches())){
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }
        Customer customer = customerDao.findById(customerAuth.getCustomer().getId());
        final String encryptedPassword = passwordCryptographyProvider.encrypt(oldPassword, customer.getSalt());
        if(!(customer.getPassword().equals(encryptedPassword))){
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }

        customer.setPassword(newPassword);
        customerDao.updateCustomerDetails(customer);
        return customer;
    }

    public Customer findByCustomerId(int id) {
        return customerDao.findById(id);
    }
}
