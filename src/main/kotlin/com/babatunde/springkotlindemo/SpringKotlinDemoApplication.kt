package com.babatunde.springkotlindemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@SpringBootApplication
class SpringKotlinDemoApplication

fun main(args: Array<String>) {
	runApplication<SpringKotlinDemoApplication>(*args)
}

data class Person(val id: UUID,val name: String,val age: Int)
data class PersonRequest(val name: String,val age: Int)
data class PersonUpdateRequest(val id: UUID,val name: String,val age: Int)

@RestController
@RequestMapping("/api/persons")
class PersonController{
	val person1 = Person(id=UUID.randomUUID(),name="Babatunde",age = 20)
    val person2 = Person(id=UUID.randomUUID(),name="Jude",age=21)
	val person3 = Person(id=UUID.randomUUID(),name="Frank",age=22)
	val person4 = Person(id=UUID.randomUUID(),name="Majid",age=23)
	val person5 = Person(id=UUID.randomUUID(),name="Jordan",age=24)
	var people: MutableMap<UUID,Person> = mutableMapOf(
	person1.id to person1,
	person2.id to person2,
	person3.id to person3,
	person4.id to person4,
	person5.id to person5)

	@GetMapping
	fun findAllPeople(): ResponseEntity<List<Person>> {
		return ResponseEntity.ok(people
			.values
			.stream()
			.sorted { p1, p2 -> p1.age - p2.age  }
			.toList()
		)
	}

	@PostMapping
	fun addNewPerson(@RequestBody personRequest: PersonRequest): ResponseEntity<Person>{
		val person = Person(id=UUID.randomUUID(),name=personRequest.name,age=personRequest.age)
		people[person.id] = person
		return ResponseEntity.ok(person)
	}

	@GetMapping("/{id}")
	fun findById(@PathVariable id: UUID): ResponseEntity<Person>{
		val person: Person = findPersonById(id) ?: return ResponseEntity.notFound().build();
		return ResponseEntity.ok(person)
	}

	@DeleteMapping("/{id}")
	fun deletePerson(@PathVariable id: UUID): ResponseEntity<String>{
		findPersonById(id) ?: return ResponseEntity.notFound().build()
		people.remove(id)
		return ResponseEntity.ok("Successfully deleted person")
	}

	@PutMapping
	fun updatePersonDetails(@RequestBody personUpdateRequest: PersonUpdateRequest): ResponseEntity<Person> {
		val id = personUpdateRequest.id
		val person = findPersonById(id) ?: return ResponseEntity.notFound().build()
		val newPersonData = person.copy(name=personUpdateRequest.name, age = personUpdateRequest.age)
		people[id] = newPersonData
		return ResponseEntity.ok(newPersonData)
	}


	fun findPersonById(id: UUID): Person?{
		return people[id]
	}
}