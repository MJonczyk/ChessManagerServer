package app.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_name")
    private String name;
}
