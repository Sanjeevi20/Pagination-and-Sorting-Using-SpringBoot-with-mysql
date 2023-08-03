package com.example.Project05.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project05.model.TModel;
import com.example.Project05.repository.MainRepository;

@RestController
@RequestMapping("/api")
public class MController {

    @Autowired
    MainRepository mainRepository;

    @GetMapping("/show_all")
    public List<TModel> getAllRows(){
        return (List<TModel>) mainRepository.findAll();

    }
    
    @PostMapping("/insert")
    public ResponseEntity<TModel> insertValues(@RequestBody TModel model){
        TModel _model = mainRepository.save(new TModel( model.getName(), model.getAge()));
        return new ResponseEntity<> (_model, HttpStatus.OK);
        
    }

    @DeleteMapping("/delete_all")
    public ResponseEntity<TModel> deleteAllRows(){
        mainRepository.deleteAll();
        return new ResponseEntity<TModel>(HttpStatus.NOT_FOUND);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<TModel> updateRow(@PathVariable("id") Long id, @RequestBody TModel model){
        Optional<TModel> _model = mainRepository.findById(id);
        if(_model.isPresent()){
            TModel __model = _model.get();
            __model.setName(model.getName());
            __model.setAge(model.getAge());
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/show/{id}")
    public Optional<TModel> findbyid(@PathVariable Long id){
        return (Optional<TModel>) mainRepository.findById(id);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItemById(@PathVariable Long id) {
        Optional<TModel> ____model = mainRepository.findById(id);
        if (____model != null) {
            mainRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    
    private Sort.Direction getSortDirection(String _sort){
        if(_sort.equals("asc")){
            return Sort.Direction.ASC;
        }
        else if(_sort.equals("desc")){
            return Sort.Direction.DESC;
        }
        else{
            return Sort.Direction.ASC;
        }
        
    }
    
    @PutMapping("/paginationandsorting")
    public ResponseEntity<Map<String,Object>> getAllRow(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "id,asc") String[] sort)
        {
            List<Order> order = new ArrayList<Order>();
            if(sort[0].contains(",")){
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    order.add(new Order(getSortDirection(_sort[1]),_sort[1]));
                    
                }
            }
            else{
                order.add(new Order(getSortDirection(sort[1]),sort[0]) );
            }

            List<TModel> models = new ArrayList<TModel>();
            Pageable pagingSort = PageRequest.of(page,size,Sort.by(order));

            Page<TModel> pageTuts;
            pageTuts = mainRepository.findAll(pagingSort);

            models = pageTuts.getContent();
            

            Map<String, Object> response = new HashMap<>();
            response.put("currentPage", pageTuts.getNumber());
            response.put("pageSize", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response,HttpStatus.OK);
   

    

}
}