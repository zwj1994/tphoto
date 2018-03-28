package com.bs.tphoto.service;/**
 * @Author:zhuwj
 * @Description:
 * @Date:Created in 10:27 2018/3/21
 */

import com.bs.tphoto.entity.PersonDO;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:zhuwj
 * @Description:
 * @Date:Created in 10:27 2018/3/21
 */
public interface PersonService {

    List<PersonDO> selectAll();

    int addPerson(PersonDO model);

}
