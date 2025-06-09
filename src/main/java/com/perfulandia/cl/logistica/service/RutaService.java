package com.perfulandia.cl.logistica.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.Ruta;
import com.perfulandia.cl.logistica.repository.RutaRepository;

@Service
public class RutaService {
   @Autowired
   RutaRepository rutaRepository;

   public List<Ruta> getAllRutas() {
      return rutaRepository.findAll();
   }

   public List<Ruta> buscarRutasPorCoordenadas(Float x_1,Float x_2,Float y_1 ,Float y_2) throws Exception{
        List<Ruta> rutas = rutaRepository.buscarRutasPorCoordenadas(x_1,x_2,y_1,y_2);
        return rutas;
   }

   public Ruta crearRuta(Ruta ruta) {
      return rutaRepository.save(ruta);
   }

   public Ruta putRuta(Ruta ruta, Integer idRuta) throws Exception{
        if(!rutaRepository.existsById(idRuta)){
            throw new RuntimeException("No existe la ruta con id: " + idRuta);
        } else {
            Optional<Ruta> rutaExistenteOptional = rutaRepository.findById(idRuta);
            Ruta rutaExistente = rutaExistenteOptional.get();
            rutaExistente.setCoordXInicio(ruta.getCoordXInicio());
            rutaExistente.setCoordYInicio(ruta.getCoordYInicio());
            rutaExistente.setCoordXFinal(ruta.getCoordXFinal());
            rutaExistente.setCoordYFinal(ruta.getCoordYFinal());
            return rutaRepository.save(rutaExistente);
        }
   }

   public Ruta parcharRuta(Ruta ruta, Integer idRuta) throws Exception{
        if(!rutaRepository.existsById(idRuta)){
            throw new RuntimeException("No existe la ruta con id: " + idRuta);
        } else {
            Optional<Ruta> rutaExistenteOptional = rutaRepository.findById(idRuta);
            Ruta rutaExistente = rutaExistenteOptional.get();
            if(ruta.getCoordXInicio() != null){
                rutaExistente.setCoordXInicio(ruta.getCoordXInicio());
            }
            if(ruta.getCoordYInicio() != null){
                rutaExistente.setCoordYInicio(ruta.getCoordYInicio());
            }
            if(ruta.getCoordXFinal() != null){
                rutaExistente.setCoordXFinal(ruta.getCoordXFinal());
            }
            if(ruta.getCoordYFinal() != null){
                rutaExistente.setCoordYFinal(ruta.getCoordYFinal());
            }
            rutaRepository.save(rutaExistente);
            return rutaExistente;
        }
   }

   public void deleteRuta(Integer idRuta) throws Exception{
        if(!rutaRepository.existsById(idRuta)){
            throw new RuntimeException("No existe la ruta con id: " + idRuta);
        } else {
            rutaRepository.deleteById(idRuta);
        }
   }

}
