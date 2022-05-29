package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Faculty;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.payload.GroupDto;
import uz.pdp.appjparelationships.repository.FacultyRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FacultyRepository facultyRepository;

    //READ FOR MINISTRY
    @GetMapping("ministry")
    public List<Group> getGroups() {
        return groupRepository.findAll();
    }

    //READ FOR UNIVERSITY DIRECTOR
    @GetMapping("/byUniversityId/{universityId}")
    public List<Group> getGroupsByUniversityId(@PathVariable Integer universityId) {
        return groupRepository.findAllByFaculty_UniversityId(universityId);
    }

    //ADD GROUP
    @PostMapping
    public String addGroup(@RequestBody GroupDto groupDto) {

        Group group = new Group();
        group.setName(groupDto.getName());

        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (!optionalFaculty.isPresent()) {
            return "Such faculty not found";
        }

        group.setFaculty(optionalFaculty.get());

        groupRepository.save(group);
        return "Group added";
    }

    //EDIT GROUP
    @PutMapping("/{id}")
    public HttpEntity<?> editGroup(@PathVariable Integer id,@RequestBody GroupDto dto){
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (!optionalGroup.isPresent()){
            return ResponseEntity.ok("Group not found");
        }
        Optional<Faculty> optionalFaculty = facultyRepository.findById(dto.getFacultyId());
        if (!optionalFaculty.isPresent()){
            return ResponseEntity.ok("Faculty not found");
        }
        Group group = optionalGroup.get();
        group.setName(dto.getName());
        group.setFaculty(optionalFaculty.get());
        groupRepository.save(group);
        return ResponseEntity.ok("Successfully edited");
    }

    //DELETE GROUP
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteGroup(@PathVariable Integer id){
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (optionalGroup.isPresent()){
            groupRepository.delete(optionalGroup.get());
            return ResponseEntity.ok("Deleted");
        }else return ResponseEntity.ok("Not found");
    }

}
