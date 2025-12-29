package com.accountservices;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @RequestMapping("/")
    String hello() {
       // return "Hello World! " +jdbcTemplate.toString();

     //   return "Hello World" + jdbcTemplate.queryForObject("SELECT dealername from azpsz1_sc.a21r_dealer where dealerid='70084' with ur",
       //  new MapSqlParameterSource(), 
         //(rs, rowNum) -> rs.getString("dealername") ).toString();

        List<List<String>> list = Arrays.asList(Arrays.asList("read","write","study"),Arrays.asList("concept","construct","contract"),Arrays.asList("work","will","word"));

        List<String > resultList=list.stream().flatMap(List::stream).filter(s -> s.startsWith("w")).map(String::toUpperCase).sorted().collect(Collectors.toList());
        resultList.forEach(System.out::println);

        System.out.println("terminal operation reduce"+list.stream().flatMap(List::stream).reduce("", (a,b) -> a +""+ b));

        System.out.println("terminal operation allMatch"+list.stream().flatMap(List::stream).allMatch(name->name.startsWith("W")));

        System.out.println("terminal operation uppercase"+list.stream().flatMap(List::stream).map(String::toUpperCase).collect(Collectors.toList()));


        list.stream().flatMap(List::stream).forEach(s->System.out.println(s));



        List<List<Integer>> numlist = Arrays.asList(Arrays.asList(1,2,5),Arrays.asList(45,22,12));

        Integer result=numlist.stream().flatMap(List::stream).mapToInt(Integer::intValue).sum();

        return result.toString();
    }

      // SQL sample
   /* @RequestMapping("calc")
    Object calc(@RequestParam int left, @RequestParam int right) {
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("left", left)
                .addValue("right", right);
        return jdbcTemplate.queryForObject("SELECT :left + :right AS answer", source,
                (rs, rowNum) -> new Result(left, right, rs.getLong("answer")));
    }*/

    @RequestMapping( method = RequestMethod.GET,value = "/getAccounts/{dealerId}")
    public List<String> getAccounts(@PathVariable String dealerId) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        
        
       List<String> accountDetails= jdbcTemplate.queryForList("SELECT accountnumber from azpsz1_sc.a21r_contract where dealerid= :dealerId fetch first 100 rows only with ur",   
       params.addValue("dealerId", dealerId),String.class);

       return accountDetails;
    }

}
