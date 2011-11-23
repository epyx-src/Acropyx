class DisableFilters
{
    def filters = {
        all(controller:'display', action:'*') {
            before = {
                return false
            }
            after = {
            }
            afterView = {
            }
        }
    }
}
