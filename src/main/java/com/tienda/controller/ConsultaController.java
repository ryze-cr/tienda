package com.tienda.controller;

import com.tienda.service.ProductoService;
import com.tienda.service.CategoriaService;
import com.tienda.domain.Categoria;
import java.util.List;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ProductoService productoService;

    private final CategoriaService categoriaService;

    public ConsultaController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    // Disponibles tambien al ejecutar cualquiera de las tres consultas de clase.
    @ModelAttribute("categorias")
    public List<Categoria> categorias() {
        return categoriaService.getCategorias(true);
    }

    @PostMapping("/consultaAmpliada")
    public String consultaAmpliada(@RequestParam(required = false) Integer idCategoria,
            @RequestParam(required = false) BigDecimal precioInf,
            @RequestParam(required = false) BigDecimal precioSup, Model model) {
        model.addAttribute("idCategoria", idCategoria);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        if (precioInf == null || precioSup == null || precioInf.signum() < 0
                || precioSup.signum() < 0 || precioInf.compareTo(precioSup) > 0
                || (idCategoria != null && idCategoria <= 0)) {
            model.addAttribute("errorConsulta", true);
            model.addAttribute("productos", List.of());
            return "/consultas/listado";
        }
        var productos = productoService.consultaAmpliada(idCategoria, precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("consultaAmpliada", true);
        return "/consultas/listado";
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        return "/consultas/listado";
    }

    @PostMapping("/consultaDerivada")
    public String consultaDerivida(@RequestParam() BigDecimal precioInf,
            @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaDerivada(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }

    @PostMapping("/consultaJPQL")
    public String consultaJPQL(@RequestParam() BigDecimal precioInf,
            @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaJPQL(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }

    @PostMapping("/consultaSQL")
    public String consultaSQL(@RequestParam() BigDecimal precioInf,
            @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaSQL(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }
}
