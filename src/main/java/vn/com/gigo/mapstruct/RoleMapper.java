package vn.com.gigo.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;

import vn.com.gigo.dtos.RoleDto;
import vn.com.gigo.entities.Role;


@Mapper(componentModel = "spring")
public interface RoleMapper {
	// ----------------------------Entity To DTO---------------------------
	RoleDto roleToRleDto(Role role);

	List<RoleDto> rolesToRoleDtos(List<Role> roles);

	// ---------------------------DTO To Entity----------------------
	Role roleDtoToRole(RoleDto roleDto);

}
