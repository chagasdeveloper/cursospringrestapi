package curso.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.model.UsuarioDTO;
import curso.api.rest.repository.UsuarioRepository;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	// Serviço RESTful
	// Encontra usuário por id
	@GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v1")
	public ResponseEntity<UsuarioDTO> initV1(@PathVariable(value = "id") Long id) {
		Usuario usuario = usuarioRepository.findById(id).get();
		System.out.println("v1");
		return new ResponseEntity<UsuarioDTO>(new UsuarioDTO(usuario), HttpStatus.OK);
	}
	
		// Serviço RESTful
		// Encontra usuário por id
		@GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v2")
		public ResponseEntity<UsuarioDTO> initV2(@PathVariable(value = "id") Long id) {
			Usuario usuario = usuarioRepository.findById(id).get();
			System.out.println("v2");
			return new ResponseEntity<UsuarioDTO>(new UsuarioDTO(usuario), HttpStatus.OK);
		}

	// lista todos os usuários
	/*
	 * Vamos supor que o carregamento de usuários seja um processo lento
	 * e queremos controlar ele com cache para agilizar o processo
	 * */
	@CacheEvict(value = "cacheusuarios", allEntries = true)
	@CachePut("cacheusuarios")
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuarios() throws InterruptedException {
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) throws Exception {
		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		/**
		// Consumindo API pública externa
		URL url = new URL("https://viacep.com.br/ws/" + usuario.getCep() + "/json/");
		URLConnection urlConnection = url.openConnection();
		InputStream is = urlConnection.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UFT-8"));
		String cep = "";
		StringBuilder jsonCep = new StringBuilder();
		
		while ((cep = br.readLine()) != null) {
			jsonCep.append(cep);
		}
		
		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
		usuario.setCep(userAux.getCep());
		usuario.setLogradouro(userAux.getLogradouro());
		usuario.setComplemento(userAux.getComplemento());
		usuario.setBairro(userAux.getBairro());
		usuario.setLocalidade(userAux.getLocalidade());
		usuario.setUf(userAux.getUf());
		
		// Fim
		*/
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha()); 
		usuario.setSenha(senhaCriptografada);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.CREATED);
	}

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		Usuario userTemp = usuarioRepository.findUserByLogin(usuario.getLogin());
		
		if (!userTemp.getSenha().equals(usuario.getSenha())) { // Senhas diferentes
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha()); 
			usuario.setSenha(senhaCriptografada);
		}
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public ResponseEntity<String> delete(@PathVariable(value = "id") Long id) {
		usuarioRepository.deleteById(id);
		return new ResponseEntity<String>("Usuário de Id: " + id + ", foi deletado com Sucesso", HttpStatus.OK);
	}
	
	
}
