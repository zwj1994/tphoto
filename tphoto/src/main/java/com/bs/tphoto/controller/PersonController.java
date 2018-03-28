package com.bs.tphoto.controller;

import com.bs.tphoto.entity.PersonDO;
import com.bs.tphoto.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 测试
 */
@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;


    /***
     * 测试
     * @return
     */
    @GetMapping("/getPersonAll")
    public List<PersonDO> getPersonAll(){
        return personService.selectAll();
    }


    /***
     * 测试
     * @return
     */
    @GetMapping("/addPerson")
    public int addPerson(){
        PersonDO model = new PersonDO();
        model.setId(new Date().getTime()+"");
        model.setName("测试");
        return personService.addPerson(model);
    }


}
