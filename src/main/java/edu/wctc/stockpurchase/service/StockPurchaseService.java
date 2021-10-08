package edu.wctc.stockpurchase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.exception.ResourceNotFoundException;
import edu.wctc.stockpurchase.repo.StockPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockPurchaseService {
    private StockPurchaseRepository stockPurchaseRepo;
    private ObjectMapper objectMapper;

    @Autowired
    public StockPurchaseService(StockPurchaseRepository sp,
                                ObjectMapper om) {
        this.stockPurchaseRepo = sp;
        this.objectMapper = om;
    }

    public StockPurchase patch(int id, JsonPatch patch) throws ResourceNotFoundException,
            JsonPatchException, JsonProcessingException {
        StockPurchase existingPurchase = getStockPurchase(id);
        JsonNode patched = patch.apply(objectMapper
                .convertValue(existingPurchase, JsonNode.class));
        StockPurchase patchedStockPurchase = objectMapper.treeToValue(patched, StockPurchase.class);
        stockPurchaseRepo.save(patchedStockPurchase);
        return patchedStockPurchase;
    }

    public void delete(int id) throws ResourceNotFoundException {
        if (stockPurchaseRepo.existsById(id)) {
            stockPurchaseRepo.deleteById(id);
        } else {
            throw new ResourceNotFoundException("StockPurchase", "id", id);
        }
    }

    public StockPurchase update(StockPurchase stockPurchase)
            throws ResourceNotFoundException {
        if (stockPurchaseRepo.existsById(stockPurchase.getId())) {
            return stockPurchaseRepo.save(stockPurchase);
        } else {
            throw new ResourceNotFoundException("StockPurchase", "id", stockPurchase.getId());
        }
    }

    public StockPurchase save(StockPurchase stockPurchase) {
        return stockPurchaseRepo.save(stockPurchase);
    }

    public List<StockPurchase> getAllStock() {
        List<StockPurchase> list = new ArrayList<>();
        stockPurchaseRepo.findAll().forEach(list::add);
        return list;
    }

    public StockPurchase getStockPurchase(int id) throws ResourceNotFoundException {
        return stockPurchaseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockPurchase", "id", id));
    }
}
