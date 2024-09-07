package sesv.dev.application;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sesv.dev.domain.GameBoardService;

@Controller
@RequestMapping("/api/colour-memory-game")
@AllArgsConstructor
public class ColourMemoryController {


    private GameBoardService gameBoardService;

    @PostMapping("/start-game")
    @ResponseBody
    public String startNewGame() {
        return gameBoardService.startGame();
    }


    @PostMapping("/play/drawCard")
    @ResponseBody
    public String drawCard(@Valid @RequestBody CardsRequest cards) {
        return gameBoardService.playGame(cards.card1(), cards.card2());
    }
}
