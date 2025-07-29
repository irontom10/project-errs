
function Header() {
  const foods = ["apple", "orange", "tomato", "potato", "potato chips"];
  return (
    <header>
      <h1 className="text-1xl font-bold underline">
        Test text v2
      </h1>
      <div>
        <ul>
          {foods.map((food, index) => (
            <li key={index}>We have {food}</li>
          ))}
        </ul>

      </div>
    </header>
  )
}

export default Header
