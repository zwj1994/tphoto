package com.bs.tphoto.service.impl;/**
 * @Author:zhuwj
 * @Description:
 * @Date:Created in 10:28 2018/3/21
 */

import com.bs.tphoto.dao.PersonMapper;
import com.bs.tphoto.entity.PersonDO;
import com.bs.tphoto.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:zhuwj
 * @Description:
 * @Date:Created in 10:28 2018/3/21
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapper personMapper;

    @Override
    public List<PersonDO> selectAll() {
        return personMapper.selectAll();
    }

    @Override
    public int addPerson(PersonDO model) {
        return personMapper.insert(model);
    }
}
