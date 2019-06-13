package org.launchcode.controllers;

import org.launchcode.models.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("search")
public class SearchController {

    @RequestMapping(value = "")
    public String search(Model model) {
        model.addAttribute("columns", ListController.columnChoices);
        return "search";
    }

    // TODO #1 - Create handler to process search request and display results


    @RequestMapping(value = "results", method = RequestMethod.POST)
    public String searchColumnValues(Model model, @RequestParam String searchType, @RequestParam String searchTerm) {

        if (searchType.equals("all")) {

            ArrayList<HashMap<String, String>> jobs = JobData.findAll();
            ArrayList<HashMap<String, String>> search_items = new ArrayList<HashMap<String, String>>();
            ArrayList<HashMap<String,String>> jobs_unique = new ArrayList<>();
            ArrayList<String> items = JobData.findAll(searchType);
            model.addAttribute("title", "All Jobs");
            model.addAttribute("jobs", jobs_unique);
            model.addAttribute("search_items", search_items);
            model.addAttribute("columns", ListController.columnChoices);
            model.addAttribute("items", jobs);
            model.addAttribute("column", searchType);


            for (HashMap<String, String> job : jobs) {
                Collection<String> job_values = job.values();
                ArrayList<String> job_values_lower = new ArrayList<>();
                for (String value : job_values) {
                    String value_lower = value.toLowerCase();
                    job_values_lower.add(value_lower);
                }
                for (String value : job_values_lower) {
                    if (value.contains(searchTerm.toLowerCase())) {
                        search_items.add(job);

                    }
                }
                for (HashMap<String, String> search_job : search_items) {

                    if (!jobs_unique.contains(search_job)) {
                        jobs_unique.add(search_job);
                    }
                }

            }

            String jobs_count = Integer.toString(jobs_unique.size()) + " Result(s)";
            model.addAttribute("jobs_count", jobs_count);

            return "search";
        } else {
            ArrayList<HashMap<String, String>> someJobs = JobData.findByColumnAndValue(searchType, searchTerm);
            ArrayList<HashMap<String,String>> jobs_unique = new ArrayList<>();
            ArrayList<String> items = JobData.findAll(searchType);
            ArrayList<String> search_items = new ArrayList<>();
            HashMap<String, String> jobs = new HashMap<>();

            model.addAttribute("title", "All " + ListController.columnChoices.get(searchType) + " Values");
            model.addAttribute("column", searchType);
            model.addAttribute("items", items);
            model.addAttribute("search_items", search_items);
            model.addAttribute("jobs", jobs_unique);
            model.addAttribute("columns", ListController.columnChoices);


            //ArrayList<HashMap<String, String>> searched_jobs = new ArrayList<HashMap<String, String>>();
            ArrayList<String> search_params = new ArrayList<>();

            for (String job : items) {
                String job_lower = job.toLowerCase();
                search_params.add(job_lower);
            }

            for (String is_in : search_params) {

                if (is_in.contains(searchTerm.toLowerCase())) {
                    search_items.add(is_in);
                }
            }

            for (HashMap<String, String> job : someJobs) {
                HashMap<String, String> job_return = new HashMap<>();

                if (!jobs_unique.contains(job)) {
                    jobs_unique.add(job);
                }
            }
            String jobs_count = Integer.toString(jobs_unique.size()) + " Result(s)";
            model.addAttribute("jobs_count", jobs_count);
            /*for (HashMap<String, String> job_map : jobs_unique){

                for (String key : job_map.keySet()){
                    String value = job_map.get(key);
                    jobs.put(key, value);
                }

            }*/
            return "search";
        }

    }

}

