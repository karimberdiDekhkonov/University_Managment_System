package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    StudentRepository studentRepository;


    //1. MINISTRY
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListByFacultyId(@PathVariable Integer facultyId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> group_facultyId = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return group_facultyId;
    }


    //4. GROUP OWNER
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentListByGroupId(@PathVariable Integer groupId,
                                                 @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> allByGroupId = studentRepository.findAllByGroupId(groupId, pageable);
        return allByGroupId;
    }
    //EDIT STUDENT
    @PutMapping
    public String editStudent(@PathVariable Integer id, @RequestBody StudentDto studentDto) {
        Optional<Address> addressOptional = addressRepository.findById(studentDto.getAddressId());
        if (!addressOptional.isPresent()) {
            return "Address id is not found";
        }

        Optional<Group> groupOptional = groupRepository.findById(studentDto.getGroupId());
        if (!groupOptional.isPresent()) {
            return "Group id is not found";
        }

        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (!optionalStudent.isPresent()){
            return "Student not found";
        }

        Student student = optionalStudent.get();
        List<Subject> list = new ArrayList<>();
        for (int i : StudentDto.subjectId) {
            Optional<Subject> optional = subjectRepository.findById(i);
            optional.ifPresent(list::add);
        }
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setAddress(addressOptional.get());
        student.setGroup(groupOptional.get());
        student.setSubjects(list);
        studentRepository.save(student);
        return "Student saved";
    }

    //ADD STUDENT
    @PostMapping
    public String addStudent(@RequestBody StudentDto studentDto) {
        Optional<Address> addressOptional = addressRepository.findById(studentDto.getAddressId());
        if (!addressOptional.isPresent()) {
            return "Address id is not found";
        }

        Optional<Group> groupOptional = groupRepository.findById(studentDto.getGroupId());
        if (!groupOptional.isPresent()) {
            return "Group id is not found";
        }

        Student student = new Student();
        List<Subject> list = new ArrayList<>();
        for (int i : StudentDto.subjectId) {
            Optional<Subject> optional = subjectRepository.findById(i);
            optional.ifPresent(list::add);
        }
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setAddress(addressOptional.get());
        student.setGroup(groupOptional.get());
        student.setSubjects(list);
        studentRepository.save(student);
        return "Student saved";
    }

    //DELETE STUDENT
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        Optional<Student> optional = studentRepository.findById(id);
        if (optional.isPresent()) {
            studentRepository.delete(optional.get());
            return "Deleted";
        } else return "Id is not found";
    }

}
