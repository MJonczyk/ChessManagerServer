package app.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "chess_events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_name")
    private String name;
}
