package by.gstu.beans;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users")
public abstract class Person implements Serializable {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "FullName")
    private String fullName;

    public Person(){}
    public Person(int id, String fullName){
        this.id = id;
        this.fullName = Objects.requireNonNull(fullName);
    }
    public Person(String fullName){
        this.fullName = Objects.requireNonNull(fullName);
    }

    public void setId(int id){this.id = id;}
    public void setFullName(String fullName){
        this.fullName = Objects.requireNonNull(fullName);
    }

    public int getId(){return id;}
    public String getFullName(){return fullName;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return getId() == person.getId() &&
                Objects.equals(getFullName(), person.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFullName());
    }

    @Override
    public String toString() {
        return "{id: " +id+ ", fullName: " +fullName+"";
    }
}
