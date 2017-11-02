package seo.dale.practice.aws.dynamodb.self.high;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@DynamoDBTypeConverted(converter=CurrencyFormat.Converter.class)
public @interface CurrencyFormat {
    String separator() default " ";

    class Converter implements DynamoDBTypeConverter<String, Currency> {
        private final String separator;
        public Converter(final Class<Currency> targetType, final CurrencyFormat annotation) {
            this.separator = annotation.separator();
        }
        public Converter() {
            this.separator = "|";
        }
        @Override
        public String convert(final Currency o) {
            return String.valueOf(o.getAmount()) + separator + o.getUnit();
        }
        @Override
        public Currency unconvert(final String o) {
            final String[] strings = o.split(separator);
            final Currency currency = new Currency();
            currency.setAmount(Double.valueOf(strings[0]));
            currency.setUnit(strings[1]);
            return currency;
        }
    }
}
