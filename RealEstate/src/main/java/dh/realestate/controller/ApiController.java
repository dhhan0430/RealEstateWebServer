package dh.realestate.controller;

import dh.realestate.model.dto.RealEstateDto;
import dh.realestate.service.RealEstateInvestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/realestate")
@RequiredArgsConstructor
public class ApiController {

    private final RealEstateInvestService realEstateInvestService;
//
//    @GetMapping("/search")
//    public RealEstateDto search(@RequestParam String region,
//                                @RequestParam String type,
//                                @RequestParam Integer lowPrice,
//                                @RequestParam Integer highPrice,
//                                @RequestParam Integer buildingYear) {
//
//        return realEstateInvestService.search(
//                region, type, lowPrice, highPrice, buildingYear);
//    }
//
//    @PostMapping("/add")
//    public RealEstateDto add(@RequestBody RealEstateDto realEstateDto) {
//
//        return realEstateInvestService.add(realEstateDto);
//    }
//
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
