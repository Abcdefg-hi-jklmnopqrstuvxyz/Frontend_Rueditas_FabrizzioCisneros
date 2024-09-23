package com.Frontend_Transporte.vehiculo.controller;

import com.Frontend_Transporte.vehiculo.dto.BuscarRequestDTO;
import com.Frontend_Transporte.vehiculo.dto.BuscarResponseDTO;
import com.Frontend_Transporte.vehiculo.viewmodel.BuscarModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/vehiculo")
public class BuscarController {
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/inicio")
    public String login(Model model) {
        model.addAttribute("buscarModel", new BuscarModel("00", "", ""));
        return "inicio";
    }

    @PostMapping("/autenticar")
    public String buscar(@RequestParam("placa") String placa, Model model) {
        // Validar campos de entrada
        if (placa == null || placa.trim().isEmpty()) {
            BuscarModel buscarModel = new BuscarModel("01", "Error: El campo es requerido.", "");
            model.addAttribute("buscarModel", buscarModel);
            return "inicio";
        }


        // Invocar API de validación de usuario
        String endpoint = "http://localhost:9002/vehiculos/buscar";
        BuscarRequestDTO buscarRequestDTO = new BuscarRequestDTO(placa);
        BuscarResponseDTO buscarResponseDTO = restTemplate.postForObject(endpoint, buscarRequestDTO, BuscarResponseDTO.class);

        // Validar respuesta
        if (buscarResponseDTO != null && buscarResponseDTO.codigo().equals("00")) {
            model.addAttribute("marca", buscarResponseDTO.marca());
            model.addAttribute("modelo", buscarResponseDTO.modelo());
            model.addAttribute("nroAsientos", buscarResponseDTO.nroAsientos());
            model.addAttribute("precio", buscarResponseDTO.precio());
            model.addAttribute("color", buscarResponseDTO.color());
            return "principal"; // Asegúrate de que este sea el nombre correcto de tu vista
        } else {
            BuscarModel buscarModel = new BuscarModel("02", "Debe ingresar una placa correcta", "");
            model.addAttribute("buscarModel", buscarModel);
            return "inicio";
        }
    }

}
