package GiostriConstrucoes.dev.repository;

import GiostriConstrucoes.dev.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.cpf = :cpf")
    boolean existsByCpf(@Param("cpf") String cpf);

    @Query("SELECT u FROM Usuario u WHERE u.email = :emailOrCpf OR u.cpf = :emailOrCpf")
    Optional<Usuario> findByEmailOrCpf(@Param("emailOrCpf") String emailOrCpf);
}