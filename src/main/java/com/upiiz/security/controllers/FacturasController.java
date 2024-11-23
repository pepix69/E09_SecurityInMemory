package com.upiiz.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
public class FacturasController {

    @GetMapping("/listar")
    public String listar(){
        return "Lista de facturas - Sin seguridad";
    }

    @GetMapping("/actualizar")
    public String actualizar(){
        return "Actualizado - Con seguridad";
    }

    @GetMapping("/eliminar")
    public String eliminar(){
        return "Factura eliminada - Con seguridad";
    }

    @GetMapping("/crear")
    public String crear(){
        return "Factura eliminada - Con seguridad";
    }

}
