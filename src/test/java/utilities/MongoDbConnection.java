package utilities;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.typesafe.config.Config;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static java.time.Instant.now;

public class MongoDbConnection {

    private static final Logger logger = LoggerFactory.getLogger(MongoDbConnection.class);
    private static Config conf = ConfigLoader.load();
    MongoClientURI uri = null;
    MongoClient mongoClient = null;
    MongoDatabase database = null;
    MongoCollection<Document> clientPolicyCollection, tNcCollection, clientDetailCollection, activeClientCollection,clientPolicy2Collection, accountCollection;


    public void connectPolicy() {
        uri = new MongoClientURI(conf.getString("mongodbPolicyURI"));
        mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase("policy");
        logger.info("Connected to database");
    }

    public void connectMarket() {
        uri = new MongoClientURI(conf.getString("mongodbMarketURI"));
        mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase("market");
        logger.info("Connected to database");
    }

    public void connectVendor() {
        uri = new MongoClientURI(conf.getString("mongodbVendorURI"));
        mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase("vendor");
        logger.info("Connected to database");
    }

    public void connectTOS() {
        uri = new MongoClientURI(conf.getString("mongodbTOSURI"));
        mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase("tos");
        logger.info("Connected to database");
    }

    public void connectAccounts() {
        uri = new MongoClientURI(conf.getString("mongodbAccountsURI"));
        mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase("accounts");
        logger.info("Connected to database");
    }

    public MongoCollection queryClientDetail() {
        logger.info("******************************** Client Detail ************************************************");
        clientDetailCollection = database.getCollection("client-detail");
        return clientDetailCollection;
    }

    public MongoCollection queryAccount() {
        logger.info("******************************** Accounts ************************************************");
        accountCollection = database.getCollection("account");
        return accountCollection;
    }

    public MongoCollection queryActiveClients() {
        logger.info("******************************** Accounts ************************************************");
        activeClientCollection = database.getCollection("activeClients");
        return activeClientCollection;
    }


    //Fetching client policy documents
    public MongoCollection queryClientPolicy() {
        logger.info("******************************** Client Policy ************************************************");
        clientPolicyCollection = database.getCollection("client-policy");
        return clientPolicyCollection;
    }

    //Fetching T&C documents
    public MongoCollection queryTermsAndConditions() {
        logger.info("******************************** Terms And Conditions ************************************************");
        tNcCollection = database.getCollection("terms-and-conditions");
        return tNcCollection;
    }

    public MongoCollection queryClientPolicyV2() {
        logger.info("******************************** Client Policy v-2 ************************************************");
        clientPolicy2Collection = database.getCollection("client-policy-v2");
        return clientPolicy2Collection;
    }


    public MongoCollection queryMarketRegulations() {
        logger.info("******************************** details-and-regulations ************************************************");
        return database.getCollection("regulation");
    }

//    public MongoCollection queryMarketDetail() {
//        logger.info("******************************** details-and-regulations ************************************************");
//        return database.getCollection("detail");
//    }

    public MongoCollection queryVendorAgreements() {
        logger.info("******************************** Vendor agreements ************************************************");
        return database.getCollection("agreements");
    }
    public MongoCollection queryRelationships() {
        logger.info("******************************** Vendor Relationship ************************************************");
        return database.getCollection("relationships");
    }


    public MongoCollection queryVendorDetails() {
        logger.info("********************************Vendor details ************************************************");
        return database.getCollection("details");
    }

    public void disconnect() {
        mongoClient.close();
        logger.info("Database connection closed");
    }


    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    public void fetchClientPolicy() {
        List<Document> clientPolicies1 = (List<Document>) clientPolicyCollection.find().into(new ArrayList<Document>());
        for (Document clientPolicy1 : clientPolicies1) {
        }
    }

    public String fetchClientPolicyIdForMaster() {
        String id = null;
        List<Document> clientPolicies = (List<Document>) clientPolicyCollection.find(and(eq("status.master.isApproving", true), eq("deletedAt", eq("$exists", false)), eq("createdBy", "CommercialOwner@dentsuaegis.com"), eq("approvals.financial", "FinancePartner@dentsuaegis.com"), eq("approvals.legal", "ClientLegal@dentsuaegis.com"))).into(new ArrayList<Document>());
        for (Document clientPolicy : clientPolicies) {
            id = (String) clientPolicy.get("originalId");
            break;
        }
        return id;
    }

    public String fetchPolicyIdNotForReApproval() {
        String id = null;
        List<Document> clientPolicies = (List<Document>) clientPolicyCollection.find(and(eq("status.master.isApproved", true), eq("deletedAt", eq("$exists", false)), eq("createdBy", "CommercialOwner@dentsuaegis.com"), eq("status.legal.isRejected", false), eq("status.finance.isRejected", false))).into(new ArrayList<Document>());
        for (Document clientPolicy : clientPolicies) {
            id = (String) clientPolicy.get("originalId");
            break;
        }
        return id;
    }

    public String fetchClientPolicyIdForBothLegalFinancial() {
        String id = null;
        List<Document> clientPolicies = (List<Document>) clientPolicyCollection.find(and(eq("status.legal.isApproving", true), eq("deletedAt", eq("$exists", false)), eq("approvals.legal", "ClientLegal@dentsuaegis.com"), eq("status.finance.isApproving", true))).into(new ArrayList<Document>());
        for (Document clientPolicy : clientPolicies) {
            id = (String) clientPolicy.get("originalId");
            break;
        }
        return id;
    }

    public String fetchRejectedClientPolicies() {
        String id = null;
        List<Document> clientPolicies = (List<Document>) clientPolicyCollection.find(and(eq("status.master.isApproving", true), exists("deletedAt", false), exists("status.legal.policyCycle", true), or(eq("status.finance.isRejected", true), eq("status.legal.isRejected", true)))).into(new ArrayList<Document>());
        for (Document clientPolicy : clientPolicies) {
            id = (String) clientPolicy.get("originalId");
            break;
        }
        return id;
    }

    public String fetchRejectedPolicyIdForFinance() {
        String id = null;
        List<Document> clientPolicies = (List<Document>) clientPolicyCollection.find(and(eq("deletedAt", eq("$exists", false)), eq("status.finance.isRejected", true), eq("approvals.financial", "FinancePartner@dentsuaegis.com"))).into(new ArrayList<Document>());
        for (Document clientPolicy : clientPolicies) {
            id = (String) clientPolicy.get("originalId");
            break;
        }
        return id;
    }

    public String fetchTermsAndConditions() {
        String id = null;
        List<Document> tNcList = (List<Document>) tNcCollection.find(eq("deletedAt", eq("$exists", false))).into(new ArrayList<Document>());
        for (Document tNc : tNcList) {
            id = (String) tNc.get("originalId");
            break;
        }
        return id;
    }

    public String fetchTermsAndConditionsToBeDeleted() {
        String id = null;
        List<Document> tNcList = (List<Document>) tNcCollection.find(and(eq("deletedAt", eq("$exists", false)), ne("accepted-users", null), ne("originalId", "5c46f8f63890b200515b3346"))).into(new ArrayList<Document>());
        for (Document tNc : tNcList) {
            id = (String) tNc.get("originalId");
            break;
        }
        return id;
    }

    public String fetchVersionForGivenId(String id) {
        String version = null;
        List<Document> tNcList = (List<Document>) tNcCollection.find(and(eq("originalId", id), eq("deletedAt", eq("$exists", false)))).into(new ArrayList<Document>());
        for (Document tNc : tNcList) {
            version = (String) tNc.get("version");
            break;
        }
        return version;
    }

    public List<String> fetchAcceptedTermsAndConditions() {
        List<String> str = new ArrayList<>();
        String id;
        String userId;
        String[] userIds;
        String lastLogin;
        List<Document> tNcList = (List<Document>) tNcCollection.find(and(nor(size("accepted-users", 0)), exists("deletedAt", false))).into(new ArrayList<Document>());
        for (Document tNc : tNcList) {
            id = (String) tNc.get("originalId");
            str.add(0, id);
            List<Document> usersList = (List<Document>) tNc.get("accepted-users");
            for (Document user : usersList) {
                userId = user.getString("userId");
                str.add(userId);
                break;
            }
            lastLogin = now().toString();
            str.add(2, lastLogin);
            break;
        }
        return str;
    }

    public List<String> fetchUserWhoAlreadyAcceptedTnC() {
        List<String> str = new ArrayList<>();
        String id;
        String userId;
        String[] userIds;
        List<Document> tNcList = (List<Document>) tNcCollection.find(and(nor(size("accepted-users", 0)), exists("deletedAt", false))).into(new ArrayList<Document>());
        for (Document tNc : tNcList) {
            id = (String) tNc.get("originalId");
            str.add(0, id);
            List<Document> usersList = (List<Document>) tNc.get("accepted-users");
            for (Document user : usersList) {
                userId = user.getString("userId");
                str.add(userId);
                break;
            }
            break;
        }
        return str;
    }


    public List<String> fetchExpiredTermsAndConditions() {
        List<String> str = new ArrayList<>();
        String id;
        String userId;
        String lastLogin;
        String[] userIds;
        List<Document> tNcList = (List<Document>) tNcCollection.find(and(nor(size("accepted-users", 0)), exists("deletedAt", false))).into(new ArrayList<Document>());
        for (Document tNc : tNcList) {
            id = (String) tNc.get("originalId");
            str.add(0, id);
            List<Document> usersList = (List<Document>) tNc.get("accepted-users");
            for (Document user : usersList) {
                userId = user.getString("userId");
                str.add(1, userId);
                break;
            }
            lastLogin = (now().plus(-31, ChronoUnit.DAYS)).toString();
            str.add(2, lastLogin);
            break;
        }
        return str;
    }

    public List<String> fetchNotAcceptedTermsAndConditions() {
        List<String> str = new ArrayList<>();
        String id;
        String userId;
        String lastLogin;
        List<Document> tNcList = (List<Document>) tNcCollection.find(and(size("accepted-users", 0), exists("deletedAt", false))).into(new ArrayList<Document>());
        for (Document tNc : tNcList) {
            id = (String) tNc.get("originalId");
            str.add(0, id);
            userId = RandomGenerator.randomAlphanumeric(3) + "@dentsuaegis.com";
            str.add(1, userId);
            lastLogin = now().toString();
            str.add(2, lastLogin);
            break;
        }
        return str;
    }

    public String fetchDeletedTermsAndConditions() {
        String id = null;
        List<Document> tNcList = (List<Document>) tNcCollection.find(exists("deletedAt", true)).into(new ArrayList<Document>());
        for (Document tNc : tNcList) {
            id = (String) tNc.get("_id");
            break;
        }
        return id;
    }

    public String fetchTermsAndConditionsToBeAccepted() {
        String id = null;
        List<Document> tNcList = (List<Document>) tNcCollection.find(exists("deletedAt", false)).into(new ArrayList<Document>());
        for (Document tNc : tNcList) {
            id = (String) tNc.get("originalId");
            break;
        }
        return id;
    }

    public int countOfDraftPoliciesForCO() {
        List<Document> draftPolicy = (List<Document>) clientPolicyCollection.find(and(eq("status.isDraft", true), eq("createdBy", "CommercialOwner@dentsuaegis.com"), exists("deletedAt", false))).into(new ArrayList<Document>());
        return draftPolicy.size();
    }

    public int countOfActivePoliciesForCO() {
        List<Document> activePolicy = (List<Document>) clientPolicyCollection.find(and(eq("status.finance.isApproved", true), eq("status.legal.isApproved", true), eq("status.master.isApproved", true), eq("createdBy", "CommercialOwner@dentsuaegis.com"), exists("deletedAt", false))).into(new ArrayList<Document>());
        return activePolicy.size();
    }

    public int countOfActivePoliciesForFinance() {
        List<Document> activePolicy = (List<Document>) clientPolicyCollection.find(and(eq("status.finance.isApproved", true), eq("status.legal.isApproved", true), eq("status.master.isApproved", true), eq("approvals.financial", "FinancePartner@dentsuaegis.com"), exists("deletedAt", false))).into(new ArrayList<Document>());
        return activePolicy.size();
    }

    public int countOfActivePoliciesForLegal() {
        List<Document> activePolicy = (List<Document>) clientPolicyCollection.find(and(eq("status.finance.isApproved", true), eq("status.legal.isApproved", true), eq("status.master.isApproved", true), eq("approvals.legal", "ClientLegal@dentsuaegis.com"), exists("deletedAt", false))).into(new ArrayList<Document>());
        return activePolicy.size();
    }

    public int countOfPendingPoliciesForCO() {
        List<Document> pendingPolicy = (List<Document>) clientPolicyCollection.find(and(or(eq("status.finance.isApproving", true), eq("status.legal.isApproving", true)), eq("createdBy", "CommercialOwner@dentsuaegis.com"), exists("deletedAt", false))).into(new ArrayList<Document>());
        return pendingPolicy.size();
    }

    public int countOfPendingPoliciesForFinance() {
        List<Document> pendingPolicy = (List<Document>) clientPolicyCollection.find(and(or(eq("status.legal.isApproving", true), eq("status.master.isApproving", true)), eq("approvals.financial", "FinancePartner@dentsuaegis.com"), exists("deletedAt", false))).into(new ArrayList<Document>());
        return pendingPolicy.size();
    }

    public int countOfPendingPoliciesForLegal() {
        List<Document> pendingPolicy = (List<Document>) clientPolicyCollection.find(and(or(eq("status.finance.isApproving", true), eq("status.master.isApproving", true)), eq("approvals.legal", "ClientLegal@dentsuaegis.com"), exists("deletedAt", false))).into(new ArrayList<Document>());
        return pendingPolicy.size();
    }

    public int countOfPendingOnMePoliciesForCO() {
        List<Document> pendingPolicy = (List<Document>) clientPolicyCollection.find(and(eq("status.master.isApproving", true), eq("createdBy", "CommercialOwner@dentsuaegis.com"), exists("deletedAt", false))).into(new ArrayList<Document>());
        return pendingPolicy.size();
    }

    public int countOfPendingOnMePoliciesForFinance() {
        List<Document> pendingPolicy = (List<Document>) clientPolicyCollection.find(and(eq("status.finance.isApproving", true), eq("approvals.financial", "FinancePartner@dentsuaegis.com"), exists("deletedAt", false))).into(new ArrayList<Document>());
        return pendingPolicy.size();
    }

    public int countOfPendingOnMePoliciesForLegal() {
        List<Document> pendingPolicy = (List<Document>) clientPolicyCollection.find(and(eq("status.legal.isApproving", true), eq("approvals.legal", "ClientLegal@dentsuaegis.com"), exists("deletedAt", false))).into(new ArrayList<Document>());
        return pendingPolicy.size();
    }

    public String fetchIdForClientName(String clientName) {
        String id = null;
        List<Document> clientPolicies = (List<Document>) clientPolicyCollection.find(and(eq("client", clientName), exists("deletedAt", false))).into(new ArrayList<Document>());
        for (Document clientPolicy : clientPolicies) {
            id = (String) clientPolicy.get("originalId");
            break;
        }
        return id;
    }

    public String fetchClientName() {
        String clientName = null;
        List<Document> clientPolicies = (List<Document>) clientPolicyCollection.find(and(eq("status.isDraft", true), exists("deletedAt", false))).into(new ArrayList<Document>());
        for (Document clientPolicy : clientPolicies) {
            clientName = (String) clientPolicy.get("client");
            break;
        }
        return clientName;
    }

    public List<String> fetchDraftPolicy() {
        List<String> str = new ArrayList<>();
        String id;
        String clientName;
        List<Document> draftPolicies = (List<Document>) clientPolicyCollection.find(and(eq("status.isDraft", true), exists("deletedAt", false), eq("createdBy", "CommercialOwner@dentsuaegis.com"))).into(new ArrayList<Document>());
        for (Document draftPolicy : draftPolicies) {
            id = (String) draftPolicy.get("originalId");
            str.add(0, id);
            clientName = (String) draftPolicy.get("client");
            str.add(1, clientName);
            break;
        }
        return str;
    }


    public String fetchClientDetailId() {
        String id = null;
        List<Document> clientDetails = (List<Document>) clientDetailCollection.find(and(eq("clientCode", "BMW"), eq("deletedAt", eq("$exists", false)))).into(new ArrayList<Document>());
        for (Document clientDetail : clientDetails) {
            id = (String) clientDetail.get("originalId");
            break;
        }
        return id;
    }


    public String fetchClientPolicyId() {
        String id = null;
        List<Document> clientPolicies = (List<Document>) clientPolicy2Collection.find(and(eq("clientCode", "000000000000000000000001"), eq("deletedAt", eq("$exists", false)))).into(new ArrayList<Document>());
        for (Document clientPolicy : clientPolicies) {
            id = (String) clientPolicy.get("originalId");
            break;
        }
        return id;
    }

//    public String fetchActive() {
//        LookupOperation lookupOperation = LookupOperation.newLookup()
//                .from("places")
//                .localField("address.location.place._id")
//                .foreignField("_id")
//                .as("address.location.place");
//
//        Aggregation agg = newAggregation(
//                unwind("address"),
//                unwind("address.location"),
//                lookupOperation
//        );
//        db.items.aggregate([
//                {
//                        $lookup:
//        {
//            from: "user_item_history",
//                    localField: "_id",
//                foreignField: "item_id",
//                as: "item_history"
//        }
//},
//        {
//            $project:
//            {
//                item_id: 1,
//                        status: "$item_history.status",
//                    imageUrl: 1
//            }
//        }])
//    }


    public boolean fetchIsActiveClientPolicy() {
        boolean isActiveClient = false;
        List<Document> clientPolicies = (List<Document>) clientPolicy2Collection.find(and(eq("clientCode", "BMW"), eq("deletedAt", eq("$exists", false)))).into(new ArrayList<Document>());
        for (Document clientPolicy : clientPolicies) {
            isActiveClient = (boolean) clientPolicy.get("isActive");
            break;
        }
        return isActiveClient;
    }

    public String fetchClientForGlobalAccount() {
        String clientCode = null;
        Integer progress;
       List<Document> accounts = (List<Document>) accountCollection.find(and(eq("type", "global"), eq("deletedAt", null))).into(new ArrayList<Document>());
        System.out.println("Fetched accounts from DB = " + accounts.size());
        for (Document account : accounts) {
            progress = (Integer) account.get("progress");
            if (progress == 100) {
                clientCode = (String) account.get("clientCode");
                break;
            }
        }
            return clientCode;
        }

    public String fetchClientForLocalAccount() {
        String clientCode = null;
        Integer progress;
        List<Document> accounts = (List<Document>) accountCollection.find(and(eq("type", "local"), eq("deletedAt", null))).into(new ArrayList<Document>());
        System.out.println("Fetched accounts from DB = " + accounts.size());
        for (Document account : accounts) {
            progress = (Integer) account.get("progress");
            if (progress == 200) {
                clientCode = (String) account.get("clientCode");
                break;
            }
        }
        return clientCode;
    }


    public String fetchActiveClient() {
        String clientCode = null;
        Integer progress;
        List<Document> policies = (List<Document>) clientPolicy2Collection.find(and(eq("statuses.self", "Completed"),eq("statuses.finance", "Approved"),eq("statuses.legal", "Approved"), eq("deletedAt", null))).into(new ArrayList<Document>());
        System.out.println("Fetched clients from DB = " + policies.size());
        for (Document policy : policies) {
            clientCode = (String) policy.get("clientCode");
            break;
        }
        return clientCode;
    }

    public String fetchActiveClientCode() {
        String clientCode = null;
        Integer progress;
        List<Document> policies = (List<Document>) activeClientCollection.find(eq("deletedAt", null)).into(new ArrayList<Document>());
        System.out.println("Fetched clients from DB = " + policies.size());
        for (Document policy : policies) {
            clientCode = (String) policy.get("clientCode");
            break;
        }
        System.out.println(clientCode);
        return clientCode;
    }

        public boolean fetchIsActiveClientDetails () {

            boolean isActiveClient = false;
            List<Document> clientPolicies = (List<Document>) clientDetailCollection.find(and(eq("clientCode", "BMW"), eq("deletedAt", eq("$exists", false)))).into(new ArrayList<Document>());
            for (Document clientPolicy : clientPolicies) {
                isActiveClient = (boolean) clientPolicy.get("isActive");
                break;
            }
            return isActiveClient;
        }

    }

