package app.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "chess_openings")
public class ChessOpening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chess_opening_id")
    private Long id;

    @Column(name = "chess_opening_code")
    private String code;

    @Column(name = "chess_opening_name")
    private String name;

    @Column(name = "moves")
    private String moves;
}
