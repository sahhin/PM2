class Object
  def abstract()
    raise AbstractMethodError, "#{self.class} muss die abstrakte Methode #{caller[0].split(" ")[-1]} implementieren"
  end
end

class AbstractMethodError < StandardError
end

module Counter

  # Inkrementiert den Zählerstand
  def inc()
    abstract()
  end

  # Gibt den Startwert des Zählers zurück

  def start()
    abstract()
  end
  # Gibt den aktuellen Zählerstand zurück
  def tally()
    abstract()
  end

  # Anzahl der Inkremente
  def num_inc()
    tally()-start()
  end

  def reset()
    abstract()
  end

  def to_s()
    "#{self.class.name} start:#{start()} tally:#{tally()} num_inc:#{num_inc()}"
  end

end