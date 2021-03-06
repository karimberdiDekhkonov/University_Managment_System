package uz.pdp.appjparelationships.payload;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class StudentDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    //ONE student TO ONE address, ONE address TO ONE student
    private int addressId;

    private int groupId;

    public static int[] subjectId;
}
