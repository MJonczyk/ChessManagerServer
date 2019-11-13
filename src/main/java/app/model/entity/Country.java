package app.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue
    @Column(name = "country_id")
    private Long id;

    @Column(name = "country_code")
    private String code;

    @Column(name = "country_name")
    private String name;


}
