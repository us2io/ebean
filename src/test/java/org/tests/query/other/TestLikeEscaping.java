package org.tests.query.other;

import io.ebean.BaseTestCase;
import io.ebean.Ebean;
import org.tests.model.basic.Customer;
import org.tests.model.basic.ResetBasicData;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestLikeEscaping extends BaseTestCase {


  @Test
  public void testLikeEscaping() {
    ResetBasicData.reset();
    // Mysql, PgSql, H2 special chars: "_"  "%"
    // MsSql special chars:  "_"  "%"  "[" AND different Quoting! 
    Ebean.save(ResetBasicData.createCustomer("Paul % Percentage", "*Star", "[none]", 0, null));
    Ebean.save(ResetBasicData.createCustomer("(none)", "More * Star", "[none]", 0, null));
    
    Ebean.save(ResetBasicData.createCustomer("Paul %% Doublepercentage", "|Pipeway", "[other]", 1, null));
    Ebean.save(ResetBasicData.createCustomer("_Udo Underscore", "|Pipeway", "[other]", 1, null));
    
    
    assertThat(Ebean.find(Customer.class)
        .where().contains("name", "Paul %%").findCount()
    ).isEqualTo(1);
    
    
    assertThat(Ebean.find(Customer.class)
        .where().startsWith("name", "_").findCount()
    ).isEqualTo(1);

    assertThat(Ebean.find(Customer.class)
        .where().startsWith("name", "_u").findCount()
    ).isEqualTo(0);
    
    assertThat(Ebean.find(Customer.class)
        .where().istartsWith("name", "_U").findCount()
    ).isEqualTo(1);

    assertThat(Ebean.find(Customer.class)
        .where().startsWith("shippingAddress.line1", "|p").findCount()
    ).isEqualTo(0);
    
    assertThat(Ebean.find(Customer.class)
        .where().startsWith("shippingAddress.line1", "|P").findCount()
    ).isEqualTo(2);
    
    
    assertThat(Ebean.find(Customer.class)
        .where().endsWith("billingAddress.line1", "]").findCount()
    ).isEqualTo(4);
    
    assertThat(Ebean.find(Customer.class)
        .where().endsWith("billingAddress.line1", "[none]").findCount()
    ).isEqualTo(2);
    
    assertThat(Ebean.find(Customer.class)
        .where().startsWith("billingAddress.line1", "[none]").findCount()
    ).isEqualTo(2);
    
    assertThat(Ebean.find(Customer.class)
        .where().contains("billingAddress.line1", "[none]").findCount()
    ).isEqualTo(2);
    
    
    assertThat(Ebean.find(Customer.class)
        .where().contains("shippingAddress.line1", "*").findCount()
    ).isEqualTo(2);
    
    assertThat(Ebean.find(Customer.class)
        .where().startsWith("shippingAddress.line1", "*").findCount()
    ).isEqualTo(1);
    
    assertThat(Ebean.find(Customer.class)
        .where().endsWith("shippingAddress.line1", "*").findCount()
    ).isEqualTo(0);
  }

}
