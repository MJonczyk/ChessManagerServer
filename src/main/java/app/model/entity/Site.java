package app.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sites")
public class Site {
    @Id
    @GeneratedValue
    @Column(name = "site_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "city")
    private String city;
}
