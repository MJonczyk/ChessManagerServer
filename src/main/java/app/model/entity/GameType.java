package app.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "game_types")
public class GameType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_type_id")
    private Long id;

    @Column(name = "game_type_name")
    private String name;
}
