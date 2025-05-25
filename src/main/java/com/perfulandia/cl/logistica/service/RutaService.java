package com.perfulandia.cl.logistica.service;

import java.util.List;

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

   public Ruta crearRuta(Ruta ruta) {
      return rutaRepository.save(ruta);
   }

   public Ruta putRuta(Ruta ruta, Integer idRuta) throws Exception{
        if(!rutaRepository.existsById(idRuta)){
            throw new RuntimeException("No existe la ruta con id: " + idRuta);
        } else {
            Ruta rutaExistente = rutaRepository.findById(idRuta).get();
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
            Ruta rutaExistente = rutaRepository.findById(idRuta).get();
            if(ruta.getCoordXInicio() != 0){
                rutaExistente.setCoordXInicio(ruta.getCoordXInicio());
            }
            if(ruta.getCoordYInicio() != 0){
                rutaExistente.setCoordYInicio(ruta.getCoordYInicio());
            }
            if(ruta.getCoordXFinal() != 0){
                rutaExistente.setCoordXFinal(ruta.getCoordXFinal());
            }
            if(ruta.getCoordYFinal() != 0){
                rutaExistente.setCoordYFinal(ruta.getCoordYFinal());
            }
            return rutaRepository.save(rutaExistente);
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
