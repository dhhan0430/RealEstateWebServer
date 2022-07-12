package dh.realestate.controller;

import dh.realestate.model.dto.RealEstateInfo;
import dh.realestate.model.dto.RealEstateList;
import dh.realestate.service.RealEstateInvestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/realestate")
@RequiredArgsConstructor
public class ApiController {

    private final RealEstateInvestService realEstateInvestService;

    @GetMapping("/search")
    public RealEstateList search(@RequestParam String region,
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
    public RealEstateInfo add(@RequestBody RealEstateInfo realEstateInfo) {

        System.out.println(realEstateInfo);

        return realEstateInvestService.add(realEstateInfo);
    }

    @PutMapping("/update")
    public RealEstateInfo update(@RequestBody RealEstateInfo realEstateInfo) {

        System.out.println(realEstateInfo);

        return realEstateInvestService.update(realEstateInfo);
    }

    @GetMapping("/find/all")
    public List<RealEstateInfo> findList() {

        return realEstateInvestService.findList();
    }

    @DeleteMapping("/delete/{index}")
    public RealEstateInfo delete(@PathVariable Long id) {

        return realEstateInvestService.delete(id);
    }

//    @DeleteMapping("/delete/all")
//    public void deletaAll() {
//
//        realEstateInvestService.deleteAll();
//    }

}
