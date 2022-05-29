package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.payload.AddressDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import java.util.Optional;

@RestController
@RequestMapping("/Address")
public class AddressController {
    @Autowired
    AddressRepository addressRepository;

    @PostMapping
    public HttpEntity<?> addAddress(@RequestBody AddressDto dto){
        Address address = new Address();
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setStreet(dto.getStreet());
        addressRepository.save(address);
        return ResponseEntity.ok("Successfully saved !");
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editAddress(@PathVariable Integer id , @RequestBody AddressDto dto){
        Optional<Address> optional = addressRepository.findById(id);
        if (!optional.isPresent()){
            return ResponseEntity.ok("Address is not found");
        }
        Address address = optional.get();
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setStreet(dto.getStreet());
        addressRepository.save(address);
        return ResponseEntity.ok("Successfully edited !");
    }

    @GetMapping
    public HttpEntity<?> getAddresses(@RequestParam int page){
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Address> all = addressRepository.findAll(pageRequest);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getAddress(@PathVariable Integer id){
        Optional<Address> optional = addressRepository.findById(id);
        if (optional.isPresent())return ResponseEntity.ok(optional.get());
        else return ResponseEntity.ok("Not found");
    }
}
