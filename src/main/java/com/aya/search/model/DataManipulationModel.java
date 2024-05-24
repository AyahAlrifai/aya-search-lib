package com.aya.search.model;

import lombok.Data;
import lombok.Setter;
import java.util.List;

/**
 * Data Manipulation Model.
 *
 * @author Ayah Refai
 * @since 03/04/2024
 */
@Data
public class DataManipulationModel {

    /**
     * list of SortDataModel, The sorting will be performed sequentially based on the order of the
     * models in the list. The first added model will be executed first,
     * followed by the next, and so on.
     */
    private List<SortDataModel> sortDataModels;

    /**
     * Can be FilterCriteria or FilterGroup.
     **/
    @Setter
    private Filter criteria;

    /**
     * set Sort Model.
     *
     * @param sortModel sortModel
     */
    public void setSortModel(final SortDataModel... sortModel) {
        sortDataModels = List.of(sortModel);
    }
}
