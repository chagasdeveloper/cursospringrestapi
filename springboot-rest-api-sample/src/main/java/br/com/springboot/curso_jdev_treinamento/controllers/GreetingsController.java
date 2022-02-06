package br.com.springboot.curso_jdev_treinamento.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.curso_jdev_treinamento.model.Usuario;
import br.com.springboot.curso_jdev_treinamento.repository.UsuarioRepository;

/**
 *
 * A sample greetings controller to return greeting text
 */
@RestController
public class GreetingsController {
	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 *
	 * @param name the name to greet
	 * @return greeting text
	 */
	@RequestMapping(value = "/init/{name}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String greetingText(@PathVariable String name) {
		return "Curso Spring Boot API  " + name + "!";
	}

	@RequestMapping(value = "/olamundo/{nome}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String retornaOlaMundo(@PathVariable String nome) {
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuarioRepository.save(usuario);
		return "Olá mundo: " + nome;
	}

	@GetMapping(value = "listatodos") // primeiro método da API
	@ResponseBody // retorna os dados para o corpo da resposta
	public ResponseEntity<List<Usuario>> listaUsuario() {
		List<Usuario> usuarios = usuarioRepository.findAll(); // executa a consulta no banco de dados
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK); // retorna a lista em JSON
	}

	@PostMapping(value = "salvar") // mapeia a URL
	@ResponseBody // Descrição da resposta
	public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario) { // recebe os dados para salvar
		Usuario user = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(user, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "atualizar") // mapeia a URL
	@ResponseBody // Descrição da resposta
	public ResponseEntity<?> atualizar(@RequestBody Usuario usuario) { // recebe os dados para salvar
		if (usuario.getId() == null) {
			return new ResponseEntity<String>("Id não foi informado", HttpStatus.OK);
		}
		Usuario user = usuarioRepository.saveAndFlush(usuario);
		return new ResponseEntity<Usuario>(user, HttpStatus.OK);
	}

	@DeleteMapping(value = "delete") // mapeia a URL
	@ResponseBody // Descrição da resposta
	public ResponseEntity<String> delete(@RequestParam Long idUser) { // recebe os dados para salvar
		usuarioRepository.deleteById(idUser);
		return new ResponseEntity<String>("User deletado com Sucesso", HttpStatus.OK);
	}

	@GetMapping(value = "buscaruserid") // mapeia a URL
	@ResponseBody // Descrição da resposta
	public ResponseEntity<Usuario> buscarUserId(@RequestParam(name = "idUser") Long idUser) { // recebe os dados para salvar
		Usuario usuario = usuarioRepository.findById(idUser).get();
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}
	
	@GetMapping(value = "buscarPorNome") // mapeia a URL
	@ResponseBody // Descrição da resposta
	public ResponseEntity<List<Usuario>> buscarPorNome(@RequestParam(name = "name") String name) { // recebe os dados para salvar
		List<Usuario> list = usuarioRepository.buscarPorNome(name.trim().toUpperCase());
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	

}
