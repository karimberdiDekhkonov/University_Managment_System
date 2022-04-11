package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.*;

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


    //1. VAZIRLIK
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
    public Page<Student> getStudentListByFacultyId(@PathVariable Integer facultyId, @RequestParam int page){
        Pageable pageable = PageRequest.of(page,10);
        Page<Student> group_facultyId = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return group_facultyId;
    }


    //4. GROUP OWNER
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentListByGroupId(@PathVariable Integer groupId,
                                                 @RequestParam int page){
        Pageable pageable = PageRequest.of(page,10);
        Page<Student> allByGroupId = studentRepository.findAllByGroupId(groupId, pageable);
        return allByGroupId;
    }

    @PutMapping
    public String editStudent(@PathVariable Integer id,@RequestBody StudentDto studentDto){
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()){
            Optional<Address> addressOptional = addressRepository.findById(studentDto.getAddressId());
            if (addressOptional.isPresent()){
                Optional<Group> groupOptional = groupRepository.findById(studentDto.getGroupId());
                if (groupOptional.isPresent()){
                    Optional<Subject> subjectOptional = subjectRepository.findById(studentDto.getSubjectId());
                    if (subjectOptional.isPresent()){
                        Student student = studentOptional.get();
                        student.setFirstName(studentDto.getFirstName());
                        student.setLastName(studentDto.getLastName());
                        student.setAddress(addressOptional.get());
                        student.setGroup(groupOptional.get());
                        student.setSubjects(subjectOptional.get());
                        studentRepository.save(student);
                        return "Student saved";
                    }else return "Subject id is not found";
                }else return "Group id is not found";
            }else return "Address id is not found";
        }else return "Student id is not found";

    }


    @PostMapping
    public String addStudent(@RequestBody StudentDto studentDto){
        Optional<Address> addressOptional = addressRepository.findById(studentDto.getAddressId());
        if (addressOptional.isPresent()){
            Optional<Group> groupOptional = groupRepository.findById(studentDto.getGroupId());
            if (groupOptional.isPresent()){
                Optional<Subject> subjectOptional = subjectRepository.findById(studentDto.getSubjectId());
                if (subjectOptional.isPresent()){
                    Student student = new Student();
                    student.setFirstName(studentDto.getFirstName());
                    student.setLastName(studentDto.getLastName());
                    student.setAddress(addressOptional.get());
                    student.setGroup(groupOptional.get());
                    student.setSubjects(subjectOptional.get());
                    studentRepository.save(student);
                    return "Student saved";
                }else return "Subject id is not found";
            }else return "Group id is not found";
        }else return "Address id is not found";
    }


    @DeleteMapping
    public String deleteStudent(@PathVariable Integer id){
        Optional<Student> optional = studentRepository.findById(id);
        if (optional.isPresent()){
            studentRepository.delete(optional.get());
            return "Deleted";
        }else return "Id is not found";
    }

}
