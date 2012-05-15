package com.edison.test.weights;

/**
 *
 */
public class Weights implements Comparable<Weights>{

    private Double weight;

    /**
     * @return the weight
     */
    public Double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Weights [weight=" + weight + "]";
    }

    /**
     * @param weight
     */
    public Weights(Double weight) {
        super();
        this.weight = weight;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Weights o) {
        return this.weight.compareTo(o.getWeight());
    }
    
    
}
