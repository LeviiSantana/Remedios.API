package com.remedios.levi.curso.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.remedios.levi.curso.remeio.DadosAtualizarRemedio;
import com.remedios.levi.curso.remeio.DadosCadastroRemeio;
import com.remedios.levi.curso.remeio.DadosDetalhamentoRemedio;
import com.remedios.levi.curso.remeio.DadosListagemRemedios;
import com.remedios.levi.curso.remeio.Remedio;
import com.remedios.levi.curso.remeio.RemedioRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/remedios")


public class RemedioController {
	
	
	@Autowired
	 private RemedioRepository repository;
	
	@PostMapping
	@Transactional
	
	public ResponseEntity<DadosDetalhamentoRemedio> cadastrar(@RequestBody @Valid DadosCadastroRemeio dados, UriComponentsBuilder uriBuilder) {
			
		var remedio = new Remedio(dados);
		
		repository.save(remedio);
			
			var uri = uriBuilder.path("/remedios/{id}").buildAndExpand(remedio.getId()).toUri();
			
			return ResponseEntity.created(uri).body(new DadosDetalhamentoRemedio(remedio));
		
	}
	

	@GetMapping	
	public ResponseEntity< List<DadosListagemRemedios>> listar(){
		var lista = repository.findAllByAtivoTrue().stream().map(DadosListagemRemedios::new).toList();
			return ResponseEntity.ok(lista);
				
	}
	
	@PutMapping
	@Transactional	
	public ResponseEntity<DadosDetalhamentoRemedio> atualizar(@RequestBody @Valid DadosAtualizarRemedio dados) {
		
		var Remedio = repository.getReferenceById(dados.id()); 
		
		Remedio.atualizarinformacoes(dados);
		
		return ResponseEntity.ok(new DadosDetalhamentoRemedio(Remedio));
				
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<String> excluir (@PathVariable Long id) {
		
		repository.deleteById(id);
		
		return ResponseEntity.noContent().build();
	
			
		
	}
	
	@DeleteMapping("inativar/{id}")
	@Transactional
	
	public ResponseEntity<String> inativar(@PathVariable Long id) {
		var Remedio = repository.getReferenceById(id);
		Remedio.inativar();
		
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping	 ("ativar/{id}")
	@Transactional	
	public ResponseEntity<String> ativar(@PathVariable Long id) {
		
		var Remedio = repository.getReferenceById(id);
		Remedio.ativar();
		
		return ResponseEntity.noContent().build();
	
	
	}
	
	@GetMapping("/{id}")

	public ResponseEntity<DadosDetalhamentoRemedio> detalhar (@PathVariable Long id) {
		
		var Remedio = repository.getReferenceById(id);
		
		return ResponseEntity.ok(new DadosDetalhamentoRemedio(Remedio));
	}
}
