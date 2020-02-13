package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.Address;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuth;
import com.upgrad.FoodOrderingApp.service.entity.State;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AddressService {

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private StateDao stateDao;

    @Autowired
    private AddressDao addressDao;

    @Transactional
    public Address saveAddress(String accessToken, Address address) throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
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
        if(address.getLocality().isEmpty() || address.getFlatBuildingNumber().isEmpty() || address.getState().getUuid().isEmpty() || address.getPincode().isEmpty()
        || address.getCity().isEmpty()){
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }

        String regexPincode = "[0-9]{6}";
        Pattern pattern = Pattern.compile(regexPincode);
        Matcher matcher = pattern.matcher(address.getPincode());
        if(!(matcher.matches())){
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }
        State state = stateDao.findByUuid(address.getState().getUuid());
        if(state == null){
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        }
        address.setState(state);
        address.setUuid(UUID.randomUUID().toString());
        Address add = addressDao.saveAddress(address);
        return add;
    }

    public List<Address> getSavedAddress(String accessToken) throws AuthorizationFailedException {
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
        return addressDao.getAllSavedAddress();
    }

    @Transactional
    public Address deleteSavedAddress(String accessToken, String addressId) throws AuthorizationFailedException, AddressNotFoundException {
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
        if(addressId.isEmpty()){
            throw new AddressNotFoundException("ANF-005","Address id can not be empty");
        }
        Address address = addressDao.getAddressByUuid(addressId);
        if(address == null){
            throw new AddressNotFoundException("ANF-003","No address by this id");
        }
        addressDao.deleteAddress(address);
        return address;
    }

    public List<State> getStates() {
        return stateDao.getAllStates();
    }

    public Address getAddressById(long id) {
        return addressDao.getAddressById(id);
    }

    public State getStateById(long id) {
        return stateDao.findById(id);
    }

    public Address getAddressByUuid(String uuid){
        return addressDao.getAddressByUuid(uuid);
    }
}
