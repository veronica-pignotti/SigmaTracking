package com.sigmaspa.sigmatracking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sigmaspa.sigmatracking.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, String> {

	public Optional<User> findByEmail(String email);

	public Optional<User> findByEmailAndPassword(String email, String password);

}
