package app.controller.assembler;

import app.controller.controller.GameController;
import app.model.dto.GameDTO;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class GameResourceAssembler implements ResourceAssembler<GameDTO, Resource<GameDTO>> {

    @Override
    public Resource<GameDTO> toResource(GameDTO gameDTO) {
        return new Resource<>(gameDTO,
                linkTo(methodOn(GameController.class).getOne(gameDTO.getId())).withSelfRel(),
                linkTo(methodOn(GameController.class).getAll()).withRel("games"));
    }
}
