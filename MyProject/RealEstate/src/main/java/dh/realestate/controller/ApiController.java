package dh.realestate.controller;

import dh.realestate.model.dto.RealEstateInfo;
import dh.realestate.model.dto.RealEstateSearch;
import dh.realestate.service.RealEstateInvestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/api/realestate")
@RequiredArgsConstructor
public class ApiController {

    private final RealEstateInvestService realEstateInvestService;

    @GetMapping("/search")
    public RealEstateSearch search(@RequestParam String region,
                                   @RequestParam String type,
                                   @RequestParam(name = "low_price") Integer lowPrice,
                                   @RequestParam(name = "high_price") Integer highPrice,
                                   @RequestParam(name = "low_year") Integer lowYear,
                                   @RequestParam(name = "high_year") Integer highYear)
            throws FileNotFoundException, UnsupportedEncodingException {

        return realEstateInvestService.search(
                region, type, lowPrice, highPrice, lowYear, highYear);
    }

    @PostMapping("/add")
    public /*RealEstateInfoDto*/ void add(@RequestBody RealEstateInfo realEstateInfo) {

        System.out.println(realEstateInfo);
        //return realEstateInvestService.add(realEstateDto);
    }

//    @PutMapping("/update")
//    public RealEstateDto update(@RequestBody RealEstateDto realEstateDto) {
//
//        return realEstateInvestService.update(realEstateDto);
//    }
//
//    @GetMapping("/find/all")
//    public List<RealEstateDto> findAll() {
//
//        return realEstateInvestService.findAll();
//    }
//
//    @DeleteMapping("/delete/{index}")
//    public void delete(@PathVariable int index) {
//
//        realEstateInvestService.delete(index);
//    }
//
//    @DeleteMapping("/delete/all")
//    public void deletaAll() {
//
//        realEstateInvestService.deleteAll();
//    }

}
