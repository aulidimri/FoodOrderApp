package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.entity.Address;
import com.upgrad.FoodOrderingApp.service.entity.State;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> address(String accessToken , final SaveAddressRequest saveAddressRequest) throws SignUpRestrictedException, AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
        final Address address = new Address();
        State state = new State();
        state.setUuid(saveAddressRequest.getStateUuid());
        address.setLocality(saveAddressRequest.getLocality());
        address.setFlatBuildingNumber(saveAddressRequest.getFlatBuildingName());
        address.setCity(saveAddressRequest.getCity());
        address.setPincode(saveAddressRequest.getPincode());
        address.setState(state);
        final Address savedAddress = addressService.saveAddress(accessToken, address);
        SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(savedAddress.getUuid()).status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/address/customer")
    public ResponseEntity<AddressListResponse> getSavedAddress(String accessToken) throws AuthorizationFailedException {
        List<Address> addresses = addressService.getSavedAddress(accessToken);
        List<AddressList> addressListResponses = new ArrayList<>();
       for(Address address : addresses){
           State state = addressService.getStateById(address.getState().getId());
           AddressListState addressListState = new AddressListState().stateName(state.getStateName()).id(UUID.fromString(state.getUuid()));
           AddressList addressList = new AddressList().city(address.getCity()).id(UUID.fromString(address.getUuid()))
                   .flatBuildingName(address.getFlatBuildingNumber()).locality(address.getLocality()).pincode(address.getPincode())
                   .state(addressListState);
           addressListResponses.add(addressList);
       }
        AddressListResponse addressListResponse = new AddressListResponse().addresses(addressListResponses);
        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path= "/address/{address_id}")
    public ResponseEntity<DeleteAddressResponse> deleteSavedAddress(String accessToken, @PathVariable String address_id) throws AuthorizationFailedException, AddressNotFoundException {
        Address address = addressService.deleteSavedAddress(accessToken, address_id);
        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse().id(UUID.fromString(address.getUuid()))
                .status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/states")
    public ResponseEntity<StatesListResponse> getStates() {
        List<State> states = addressService.getStates();
        List<StatesList> statesLists = new ArrayList<>();
        for (State state : states) {
            StatesList statesList = new StatesList().id(UUID.fromString(state.getUuid())).stateName(state.getStateName());
            statesLists.add(statesList);
        }
        StatesListResponse statesListResponse = new StatesListResponse().states(statesLists);
        return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);

    }


}
