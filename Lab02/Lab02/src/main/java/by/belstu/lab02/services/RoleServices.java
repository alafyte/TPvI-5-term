package by.belstu.lab02.services;

import by.belstu.lab02.models.Role;
import by.belstu.lab02.models.Roles;
import by.belstu.lab02.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServices {
    @Autowired
    public RoleRepository roleRepository;

    public void saveRole(Role role){
        roleRepository.save(role);
    }

    public void deleteRole(int id){
        roleRepository.deleteById(id);
    }

    public Role findRole(int id){
        return roleRepository.findById(id).orElse(null);
    }

    public void updateRole(Role role){
        roleRepository.save(role);
    }

    public List<Role> getRoles(){
        return Streamable.of(roleRepository.findAll()).toList();
    }

    public Optional<Role> findByName(Roles name) {
        List<Role> roles = Streamable.of(roleRepository.findAll()).toList();
        return roles.stream().filter(role -> role.getName().equals(name)).findFirst();
    }

}
