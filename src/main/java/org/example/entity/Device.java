package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", referencedColumnName = "id")
    @Nullable
    private List<User> users;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)//(mappedBy = "device")
//    @JoinColumn(name = "id", referencedColumnName = "id")
    @Nullable
    private List<Sensor> sensorList;

    public Device(String name, @Nullable List<User> users, @Nullable List<Sensor> sensorList) {
        this.name = name;
        this.users = users;
        this.sensorList = sensorList;
    }

}
