package com.perfulandia.cl.logistica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.Ruta;
import com.perfulandia.cl.logistica.repository.RutaRepository;

/**
 * Servicio para gestionar la lógica de negocio de las rutas.
 * Proporciona métodos para crear, leer, actualizar y eliminar rutas.
 */
@Service
public class RutaService {
   @Autowired
   RutaRepository rutaRepository;

   /**
    * Obtiene una lista de todas las rutas.
    * @return una lista de todas las entidades {@link Ruta}.
    */
   public List<Ruta> getAllRutas() {
      return rutaRepository.findAll();
   }

   /**
    * Busca rutas cuyas coordenadas finales se encuentren dentro de un área rectangular.
    * @param x_1 Coordenada X inicial del área.
    * @param x_2 Coordenada X final del área.
    * @param y_1 Coordenada Y inicial del área.
    * @param y_2 Coordenada Y final del área.
    * @return una lista de {@link Ruta} que coinciden con los criterios.
    * @throws Exception si ocurre un error durante la búsqueda.
    */
   public List<Ruta> buscarRutasPorCoordenadas(Float x_1,Float x_2,Float y_1 ,Float y_2) throws Exception{
        List<Ruta> rutas = rutaRepository.buscarRutasPorCoordenadas(x_1,x_2,y_1,y_2);
        return rutas;
   }

   /**
    * Crea una nueva ruta.
    * @param ruta el objeto {@link Ruta} a crear.
    * @return la {@link Ruta} guardada.
    */
   public Ruta crearRuta(Ruta ruta) {
      return rutaRepository.save(ruta);
   }

   /**
    * Actualiza completamente una ruta existente (operación PUT).
    * @param ruta el objeto {@link Ruta} con los nuevos datos.
    * @param idRuta el ID de la ruta a actualizar.
    * @return la {@link Ruta} actualizada.
    * @throws RuntimeException si no se encuentra una ruta con el ID proporcionado.
    */
   public Ruta putRuta(Ruta ruta, Integer idRuta) {
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

   /**
    * Actualiza parcialmente una ruta existente (operación PATCH).
    * @param ruta el objeto {@link Ruta} con los campos a actualizar.
    * @param idRuta el ID de la ruta a actualizar.
    * @return la {@link Ruta} actualizada.
    * @throws RuntimeException si no se encuentra una ruta con el ID proporcionado.
    */
   public Ruta parcharRuta(Ruta ruta, Integer idRuta) {
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

   /**
    * Elimina una ruta por su ID.
    * @param idRuta el ID de la ruta a eliminar.
    * @throws RuntimeException si no se encuentra una ruta con el ID proporcionado.
    */
   public void deleteRuta(Integer idRuta) {
        if(!rutaRepository.existsById(idRuta)){
            throw new RuntimeException("No existe la ruta con id: " + idRuta);
        } else {
            rutaRepository.deleteById(idRuta);
        }
   }

}
