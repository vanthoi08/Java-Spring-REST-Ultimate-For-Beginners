package vn.hoidanit.jobhunter.domain.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
// Hàm tạo tất cả tham số
@AllArgsConstructor
// // Hàm tạo không tham số
@NoArgsConstructor
public class ResUserDTO {

    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;
    private Instant updatedAt;

    // public ResUserDTO() {
    // }

    // public ResUserDTO(long id, String name, String email, GenderEnum gender,
    // String address, int age, Instant createdAt,
    // Instant updatedAt) {
    // this.id = id;
    // this.name = name;
    // this.email = email;
    // this.gender = gender;
    // this.address = address;
    // this.age = age;
    // this.createdAt = createdAt;
    // this.updatedAt = updatedAt;
    // }

}
