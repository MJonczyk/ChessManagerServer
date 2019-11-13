package app.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "results")
public class Result {
    @Id
    @GeneratedValue
    @Column(name = "result_id")
    private Long id;

    @Column(name = "result")
    private String result;
}
