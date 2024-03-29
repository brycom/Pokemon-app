package com.example.pokemon.api;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.pokemon.model.Pokemon;
import com.example.pokemon.model.PokiDeck;
import com.example.pokemon.model.User;
import com.example.pokemon.repository.UserRepository;
import com.example.pokemon.repository.pokemonRepository;
import com.example.pokemon.repository.pokiDeckRepository;

import com.example.pokemon.service.PokiDeckService;

@RestController
@RequestMapping("/api/pokiDeck")
@CrossOrigin
public class pokiDeckController {

    public final PokiDeckService pokiDeckService;
    public final UserRepository userRepository;
    public final pokiDeckRepository pokiDeckRepository;
    public final pokemonRepository pokemonRepository;

    public pokiDeckController(PokiDeckService pokiDeckService, UserRepository userRepository,
            com.example.pokemon.repository.pokiDeckRepository pokiDeckRepository, pokemonRepository pokemonRepository) {
        this.pokiDeckService = pokiDeckService;
        this.userRepository = userRepository;
        this.pokiDeckRepository = pokiDeckRepository;
        this.pokemonRepository = pokemonRepository;
    }

    @GetMapping("/{deckId}")
    public List<Pokemon> getDeck(/* @AuthenticationPrincipal UserDetails userDetails */ @PathVariable int deckId) {

        Optional<User> userO = userRepository.findByUsername("mathias");
        User user = userO.orElse(null);

        return pokiDeckService.getPockiDeck(user.getId(), deckId);

    }

    @GetMapping("/decks")
    public Iterable<PokiDeck> getDecks() {

        return pokiDeckRepository.findAll();
    }

    @PostMapping("/newDeck/{name}")
    public PokiDeck newDeck(/* @AuthenticationPrincipal UserDetails userDetails */ @PathVariable String name) {

        Optional<User> userO = userRepository.findByUsername("mathias");
        User user = userO.orElse(null);
        PokiDeck pokiDeck = new PokiDeck(user.getId(), name);
        pokiDeckRepository.save(pokiDeck);

        return pokiDeck;
    }

    @PostMapping("/addPokemon")
    public Pokemon addPokemon(@RequestBody Pokemon Pokemon) {
        Pokemon p = new Pokemon(Pokemon.getUrl(), Pokemon.getDeckId());
        pokemonRepository.save(p);
        System.out.println(Pokemon.getDeckId());

        return p;
    }

    @DeleteMapping("/deletePokemon/{id}")
    public Optional<Pokemon> deletePokemon(@PathVariable int id) {
        System.out.println("i början");
        Optional<Pokemon> p = pokemonRepository.findById(id);
        pokemonRepository.deleteById(id);

        return p;
    }

    @PatchMapping("/addComment/{id}")
    public Pokemon addComment(@PathVariable int id, @RequestBody String comment) {
        Optional<Pokemon> pokemon = pokemonRepository.findById(id);
        Pokemon p = pokemon.orElse(null);
        p.setComment(comment);
        pokemonRepository.save(p);

        return p;
    }

}
