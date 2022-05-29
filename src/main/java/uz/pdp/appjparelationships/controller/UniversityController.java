package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.University;
import uz.pdp.appjparelationships.payload.UniversityDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.UniversityRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class UniversityController {
    @Autowired
    UniversityRepository universityRepository;
    @Autowired
    AddressRepository addressRepository;

    //READ
    @RequestMapping(value = "/university", method = RequestMethod.GET)
    public HttpEntity<?> getUniversities(@RequestParam int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<University> all = universityRepository.findAll(pageRequest);
        return ResponseEntity.ok(all);
    }

    //CREATE
    @RequestMapping(value = "/university", method = RequestMethod.POST)
    public String addUniversity(@RequestBody UniversityDto universityDto) {

        Optional<Address> optional = addressRepository.findById(universityDto.getAddressId());
        if (!optional.isPresent()) return "Address not found";

        University university = new University();
        university.setName(universityDto.getName());
        university.setAddress(optional.get());
        universityRepository.save(university);

        return "University added";
    }

    //UPDATE
    @RequestMapping(value = "/university/{id}", method = RequestMethod.PUT)
    public String editUniversity(@PathVariable Integer id, @RequestBody UniversityDto universityDto) {
        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (!optionalUniversity.isPresent()) return "University not found";
        Optional<Address> optionalAddress = addressRepository.findById(universityDto.getAddressId());
        if (!optionalAddress.isPresent()) return "Address not found";
        University university = optionalUniversity.get();
        Address address = optionalAddress.get();

        university.setName(universityDto.getName());
        university.setAddress(address);
        universityRepository.save(university);

        return "University edited";
    }

    //DELETE
    @RequestMapping(value = "/university/{id}",method = RequestMethod.DELETE)
    public String deleteUniversity(@PathVariable Integer id){
        universityRepository.deleteById(id);
        return "University deleted";
    }

    //READ ONE UNIVERSITY
    @RequestMapping(value = "university/{id}",method = RequestMethod.GET)
    public HttpEntity<?> getUniversity(@PathVariable Integer id){
        Optional<University> optional = universityRepository.findById(id);
        if (optional.isPresent()){
            return ResponseEntity.ok(optional.get());
        }else return ResponseEntity.ok("not found");
    }
}
