package curso.api.rest.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;

@Service
@Transactional
public class ImplementacaoUserDetailsService implements UserDetailsService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// consultar no banco o usuário
		Usuario usuario = usuarioRepository.findUserByLogin(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não foi encontrado");
		}
		return new User(usuario.getLogin(),
				usuario.getSenha(),
				usuario.getAuthorities());
	}

}
